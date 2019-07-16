import psycopg2 as pg
from kafka import KafkaConsumer
from datetime import datetime
import json

consumer = KafkaConsumer('anomaly_7',
                         bootstrap_servers=['localhost:9092'])

for message in consumer:
    newid = message.key.decode('utf-8')[1:].split('@')[0]
    newval = int(float(message.value.decode('utf-8')))
    date_object = datetime.now()
    current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
    with open("../temporary/othersthreshold.json", 'r') as f:
        othersinfo = json.load(f)
    categorythreshold = othersinfo["category"]
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    cur.execute("INSERT INTO others(inserttime, anomalytype, id, threshold, value) VALUES(%s, %s, %s, %s, %s)",
                (current_time, "category", newid, categorythreshold, newval))
    conn.commit()
    cur.close()
    conn.close()
            
