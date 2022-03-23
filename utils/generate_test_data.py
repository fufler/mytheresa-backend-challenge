#!/usr/bin/env python3

import argparse
import json
import sys

parser = argparse.ArgumentParser(description='Generates test data')
parser.add_argument('-p', '--products-number', required=True, type=int, help='Number of products to generate')
parser.add_argument('-c', '--categories-number', required=True, type=int, help='Number of categories to generate')
parser.add_argument('-b', '--boots', required=True, type=int, help='Number of products in boots categoriy to generate')

args = parser.parse_args()

categories = [f'Category #{i+1}' for i in range(args.categories_number)]

products = [{
    'sku': f'{i:06}',
    'name': f'Product #{i + 1}',
    'category': categories[i % args.categories_number],
    'price': 10 * (1 + i)
} for i in range(args.products_number)]

for i in range(args.boots):
    products.append({
        'sku': f'{len(products):06}',
        'name': f'Boots #{i + 1}',
        'category': 'boots',
        'price': 10 * (1 + i)
    })

json.dump({ 'products' : products }, sys.stdout, indent=True)