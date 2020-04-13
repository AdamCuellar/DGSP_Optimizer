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
    stockList = ['AAPL']

    p.map(getData, stockList)
    return


if __name__ == "__main__":
    main()