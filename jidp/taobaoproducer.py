import pandas as pd
import json
import time

if __name__=="__main__":
    data = pd.read_csv("/mnt/taobao.csv")
    for index, row in data.iterrows():
        newdict = dict()
        newdict['timestamp'] = str(row[0])
        newdict['label'] = str(row[1])
        newdict['userid'] = str(row[2])
        newdict['brand'] = row[3].split(',')
        newdict['product'] = row[4].split(',')
        newdict['category'] = row[5].split(',')
        newdict['item clicked'] = row[6].split(',')
        newdict['item viewed'] = row[7].split(',')
        newdict['shop clicked'] = row[8].split(',')
        newdict['shop viewed'] = row[9].split(',')
        output = json.dumps(newdict)
        # TODO: how to send this json string to Kafka?
        time.sleep(0.5) # send a new message every 0.5 second
        