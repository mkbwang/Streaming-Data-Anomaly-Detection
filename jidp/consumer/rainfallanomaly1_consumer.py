from kafka import KafkaConsumer
import numpy as np
import pickle
import psycopg2 as pg
from datetime import datetime
import json

consumer = KafkaConsumer('anomaly_0',
                         bootstrap_servers=['localhost:9092'])
pointlist = []
mykey = ""
for message in consumer:
    # print("new message coming")
    newkey = message.key.decode('utf-8')
    newkey = int(newkey[1:].split('@')[0])
    newval = int(float(message.value.decode('utf-8')))
    pointlist = [newkey//501, newkey%501]
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    date_object = datetime.now()
    current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
    with open('../temporary/rainthreshold.json') as f:
        threshold = json.load(f)
    cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points, threshold, value) VALUES(%s, %s, %s, %s, %s)",
                (current_time, "threshold", pointlist, threshold["threshold"], newval))
    conn.commit()
    cur.close()
    conn.close()

