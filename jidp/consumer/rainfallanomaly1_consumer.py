from kafka import KafkaConsumer
import numpy as np
import pickle
import psycopg2 as pg
from datetime import datetime
import json

consumer = KafkaConsumer('notification_job_0',
                         bootstrap_servers=['localhost:9092'])
pointlist = []
mykey = ""
for message in consumer:
    newkey = message.key.decode('utf-8')
    timestamp = newkey.split('@')[1]
    newval = message.value.decode('utf-8')
    if mykey!=timestamp:
        conn = pg.connect("dbname=anomalydb user=wmk")
        cur = conn.cursor()
        date_object = datetime.now()
        current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
        with open('../temporary/rainthreshold.json') as f:
            threshold = json.load(f)
        cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points, threshold) VALUES(%s, %s, %s ,%s)",
                    (current_time, "threshold", pointlist, threshold["threshold"]))
        conn.commit()
        cur.close()
        conn.close()
        mykey = timestamp
    else:
        position = int(message.value.decode('utf-8'))
        pointlist.append([position//501, position%501])
        # newmat.reshape((501,501))
        # print(message.key)
        # print(message.value)        
        # id += 1
        # np.save('temporary/currentrain.npy', newmat)
