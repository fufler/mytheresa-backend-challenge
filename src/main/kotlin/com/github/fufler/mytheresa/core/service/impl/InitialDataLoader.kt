package com.github.fufler.mytheresa.core.service.impl


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.fufler.mytheresa.core.service.impl.InitialDataLoader.Companion.PROP_INITIAL_DATA_PATH
import com.github.fufler.mytheresa.core.service.impl.model.CategoriesRepository
import com.github.fufler.mytheresa.core.service.impl.model.DBBackedCategory
import com.github.fufler.mytheresa.core.service.impl.model.DBBackedProduct
import com.github.fufler.mytheresa.core.service.impl.model.ProductsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import javax.annotation.PostConstruct

private data class Products(
    val products: List<Product>
) {
    data class Product(
        val name: String,
        val sku: String,
        val category: String,
        val price: Int
    )
}


@Service
@ConditionalOnProperty(PROP_INITIAL_DATA_PATH)
class InitialDataLoader(
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository
) {
    @Autowired
    @Lazy
    private lateinit var self: InitialDataLoader

    @PostConstruct
    fun init() {
        self.load()
    }

    @Transactional
    fun load() {
        val initialDataPath = System.getProperty(PROP_INITIAL_DATA_PATH) ?: return

        if (categoriesRepository.isNotEmpty() || productsRepository.isNotEmpty())
            return

        logger.info("Loading initial data from {}", initialDataPath)

        val objectMapper = jacksonObjectMapper()

        val initialDataFile = File(initialDataPath)

        require(initialDataFile.exists()) {
            "Initial data file $initialDataPath must exist"
        }

        val products = initialDataFile.reader().use { reader ->
            objectMapper.readValue<Products>(reader)
        }.products

        val categories = products
            .map { it.category }
            .distinct()

        val categoriesMap = mutableMapOf<String, Long>()

        categories
            .chunked(SAVE_CHUNK_SIZE) { items ->
                items.map {
                    DBBackedCategory(
                        name = it
                    )
                }
            }
            .forEach { items ->
                categoriesRepository
                    .saveAll(items)
                    .forEach { c ->
                        categoriesMap[c.name] = c.id
                    }
            }

        products
            .chunked(SAVE_CHUNK_SIZE) { items ->
                items.map {
                    DBBackedProduct(
                        sku = it.sku,
                        name = it.name,
                        price = it.price,
                        categoryId = categoriesMap.getValue(it.category)
                    )
                }
            }
            .forEach(productsRepository::saveAll)

    }

    companion object {
        internal const val PROP_INITIAL_DATA_PATH = "db.initialDataPath"
        private const val SAVE_CHUNK_SIZE = 100
        private val logger = LoggerFactory.getLogger(InitialDataLoader::class.java)
    }
}