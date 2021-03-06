import psycopg2 as pg
from kafka import KafkaConsumer
from datetime import datetime
import json

consumer = KafkaConsumer('anomaly_6',
                         bootstrap_servers=['localhost:9092'])

for message in consumer:
    newid = message.key.decode('utf-8')[1:].split('@')[0]
    newval = int(float(message.value.decode('utf-8').split(':')[0]))
    newthreshold = int(float(message.value.decode('utf-8').split(':')[1]))
    date_object = datetime.now()
    current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
    with open("../temporary/othersthreshold.json", 'r') as f:
        othersinfo = json.load(f)
    productthreshold = othersinfo["product"]
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    cur.execute("INSERT INTO others(inserttime, anomalytype, id, threshold, value) VALUES(%s, %s, %s, %s, %s)",
                (current_time, "product", newid, newthreshold, newval))
    conn.commit()
    cur.close()
    conn.close()
            
