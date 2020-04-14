# downloads end of day stock data (freely available at quandl) to the same location the script is in
# dependencies: pandas, quandl
#               pip install pandas
#               pip install quandl
# to run the script:
#               python downloadStocks.py
import quandl
from multiprocessing import Pool
import pandas as pd

def getData(stockSymbol):
    data = quandl.get("WIKI/" + stockSymbol)
    data.to_csv(stockSymbol + ".csv", index=False)
    return

def main():
    p = Pool()
    #Added some oil companies, pharmacutical companies working on COVID-19 vaccine,largecap technology companies, the rest after line 1 are randomly generated
    stockList = ['AAPL', 'MSFT', 'AMZN', 'TSLA', 'DIS', 'INO', 'BA', 'GDP', 'PFE', 'NVAX', 
                   'HD',  'FIS', 'MMM',  'MAR', 'WFC',  'MN', 'NKE', 'JNJ',  'KR',  'NJR', 
                     'MCD',  'INTC', 'GE',  'UNH', 'PG',  'IBM', 'V', 'RXN',  'AXP',  'JPM',   
                        'SNV',  'NRGU', 'XOM',  'APH', 'GS',  'CSCO', 'CAT', 'WMT',  'VNCE',  'IMOM', 
                           'VZ',  'MRK', 'UTX',  'JW', 'BVSN',  'NAK', 'NWL',  'GSC', 'SFYF', 'CTEST', ]

    p.map(getData, stockList)
    return


if __name__ == "__main__":
    main()