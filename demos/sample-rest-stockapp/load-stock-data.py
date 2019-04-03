'''
Setup data for the sample stock app. Just throws some random numbers for now
'''
import sys
import csv

try:
    import aerospike
except ImportError as e:
    print("Make sure the Aerospike Python client is installed: `pip install aerospike`")
    sys.exit(-1)

from aerospike_helpers.operations import list_operations as lo

NAME_COLUMN = 'Name'
CLOSE_COLUMN = 'close'
DATE_COLUMN = 'date'
OPEN_COLUMN = 'open'
PORTFOLIO_SET = 'portfolios'
RECENT_PORTFOLIOS_KEY = 'recent-portfolios'

PORTFOLIOS = {
    'faang': ['FB', 'AMZN', 'AAPL', 'NFLX', 'GOOG'],
}

def main(filename, hostname='localhost', port=3000):
    '''
    This initializes the data for our main app
    Args:
        filename string name of file.
    '''
    client = aerospike.client({'hosts': [(hostname, port)]}).connect()

    main_record_key = 'test', 'stocks', 'symbols'
    symbol_to_dates = {}
    latest_price = {}

    with open(filename) as inf:
        count = 0
        #  Write daily stock performance records.
        for row in csv.DictReader(inf):
            count += 1
            if count % 20000 == 0:
                print('{} records loaded so far.'.format(count))
            symbol = row[NAME_COLUMN]
            closing_price = row[CLOSE_COLUMN]
            opening_price = row[OPEN_COLUMN]
            date = row[DATE_COLUMN]
            if symbol not in symbol_to_dates:
                symbol_to_dates[symbol] = [date]
            else:
                symbol_to_dates[symbol].append(date)

            latest_price[symbol] = closing_price

            write_daily_record(client, symbol, date, opening_price, closing_price)

    #  Write a record containing all of the stock symbols in alphabetical order.
    symbols = list(sorted([symbol for symbol in symbol_to_dates]))
    main_record_rec = {'symbols': symbols}
    client.put(main_record_key, main_record_rec)

    #  For each symbol, write a record containing the data points available for it.
    sort_op = lo.list_set_order('dates', aerospike.LIST_ORDERED)
    for symbol in symbol_to_dates:
        key = 'test', 'stocks', symbol
        rec = {
            'symbol': symbol,
            'dates': list(sorted(symbol_to_dates[symbol])),
            'price': latest_price[symbol]
        }
        client.put(key, rec)
        client.operate(key, [sort_op])

    #  Create some sample portfolios.
    for portfolio_name in PORTFOLIOS:
        key = 'test', PORTFOLIO_SET, portfolio_name
        rec = {
            'name': portfolio_name,
            'id': portfolio_name,
            'stocks': PORTFOLIOS[portfolio_name]
        }
        client.put(key, rec)

    #  Now Enter the preloaded portfolios into the list.
    portfolio_list_key = 'test', 'stocks', RECENT_PORTFOLIOS_KEY
    recent_portfolios = [portfolio_name for portfolio_name in PORTFOLIOS]
    client.put(
        portfolio_list_key,
        {
            'portfolios': recent_portfolios
        })


def write_daily_record(client, symbol, date, opening_price, closing_price):
    '''
    Write a record for a daily entry for the specified stock symbol
    '''
    key = ('test', 'stocks', '{}-{}'.format(symbol, date))
    rec = {
        'symbol': symbol,
        'date': date,
        'close': closing_price,
        'open': opening_price
    }
    client.put(key, rec)


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("A path to the a .csv data file is required")
        print("Usage: load-stock-data.py <path_to_csv_file> [<aerospike_host>, [<aerospike_port>]]")
    csv_path = sys.argv[1]
    host = 'localhost'
    port = 3000
    if len(sys.argv) > 2:
        host = sys.argv[2]
    if len(sys.argv) == 4:
        port = int(sys.argv[3], base=10)
    main(csv_path, host, port)
