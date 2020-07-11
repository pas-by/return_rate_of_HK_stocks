#!/usr/bin/env python
# -*- coding: utf-8 -*-

#  File Name   : hkStocks5Yr.py
#  description : 列出 yahoo finance 中，
#               「所有」港股最近五年的回報率。
#        begin : 2020-07-10
#last modified : 2020-07-10

import pandas as pd
import yfinance as yf
import util

for i in range(1159, 9999):
    co = '{:0>4}.HK'.format(i)
    print(co, end=', ')
    print(util.fiveYearReturn(co, 2020))
