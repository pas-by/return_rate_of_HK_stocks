#!/usr/bin/env python
# -*- coding: utf-8 -*-

#  fetch stocks' 'code'
#  列出 yahoo finance '沒有' 資料的代碼

import pandas as pd
import yfinance as yf

for i in range(1, 9999):
    co = '{:0>4}.HK'.format(i)

    stock = yf.Ticker(co)
    print(co, end =", ")
    print(stock.info['longName'])

    #  get historical market data
    #  hist = stock.history(period="max", interval="1mo")

    #  print(hist)
