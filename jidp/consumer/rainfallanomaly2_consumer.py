from kafka import KafkaConsumer
import numpy as np
import pickle
import psycopg2 as pg
from datetime import datetime
import json


# statistical outlier
consumer = KafkaConsumer('anomaly_1',
                         bootstrap_servers=['localhost:9092'])# job name to be changed
pointlist = []
mykey = ""
for message in consumer:
    # print("new message coming")
    newkey = message.key.decode('utf-8')
    newkey = int(newkey[1:].split('@')[0])
    pointlist = [newkey//501, newkey%501]
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    date_object = datetime.now()
    current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
    cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points) VALUES(%s, %s, %s)",
                (current_time, "statistical", pointlist))
    conn.commit()
    cur.close()
    conn.close()

