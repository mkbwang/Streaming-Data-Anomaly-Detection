import psycopg2 as pg
from kafka import KafkaConsumer
from datetime import datetime
import json

consumer = KafkaConsumer('anomaly_2',
                         bootstrap_servers=['localhost:9092'])

for message in consumer:
    newid = message.key.decode('utf-8')[1:].split('@')[0]
    newval = int(message.value.decode('utf-8'))
    date_object = datetime.now()
    current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
    with open("../temporary/customerthreshold.json", 'r') as f:
        userinfo = json.load(f)
    brandthreshold = userinfo["userbrand"]
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    cur.execute("INSERT INTO customer(inserttime, anomalytype, userid, threshold, value) VALUES(%s, %s, %s, %s, %s)",
                (current_time, "brand", newid, brandthreshold, newval))
    conn.commit()
    cur.close()
    conn.close()
            
