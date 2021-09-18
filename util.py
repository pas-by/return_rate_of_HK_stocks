#!/usr/bin/env python
# -*- coding: utf-8 -*-

#  util.py
#  contains useful functions

import numpy as np 
import pandas as pd
import yfinance as yf
import math

def first_day_of_year(offset=0, d=np.datetime64('today')):
    dt = '{}-01-01'.format(str(d.astype(object).year + offset))
    dt = np.datetime64(dt)

    #  turn it into string
    dt = np.datetime_as_string(dt, unit='D')
    return dt

#  讀出某股票，於某年的記錄
def hist_data(stockCode, year):
    stock = yf.Ticker(stockCode)
    hist = stock.history(start=str(year) + '-01-01', end=str(year+1)+'-01-01', interval="1mo")

    #  碼農的測試碼
    #  print(hist)

    #  找出並處理「股息」
    for x in range(hist.index.array.size-1, -1, -1):
        if pd.isna(hist.iloc[x]['Open']):
           hist.iloc[x-1, hist.columns.get_loc('Dividends')] += hist.iloc[x]['Dividends']

           #  remark : 可能連續出現派發股息

           #  remove this row
           frames = [hist.iloc[0:x],  hist.iloc[x+1:,]]
           hist = pd.concat(frames)
        else:
            #  add the dividend to the 'open price'
            hist.iloc[x, hist.columns.get_loc('Open')] += hist.iloc[x]['Dividends']
            hist.iloc[x, hist.columns.get_loc('Dividends')] = 0

    return hist

#  算出某股票，過往五年的回報率
def fiveYearReturn(stockCode, year):
    retList = []

    for x in range(5, 0, -1):
        histData = hist_data(stockCode, year-x)

        #  碼農的測試碼
        #  print(stockCode + ", " + str(year-x))

        ret = 0
        if histData.empty or histData.index.array.size<2 :
            ret = float('nan')  #  cannot compute return rate

        else:
            ret = math.log(histData.iloc[histData.index.array.size-1]['Open'] / histData.iloc[0]['Open'])

        retList.append(ret)

        #  碼農的測試碼
        #  print('return rate : ' + str(ret))
        #  print(histData)
        #  print()

    return retList
