import psycopg2 as pg
from kafka import KafkaConsumer
from datetime import datetime
import json

consumer = KafkaConsumer('anomaly_4',
                         bootstrap_servers=['localhost:9092'])

for message in consumer:
    newid = message.key.decode('utf-8')
    newval = int(message.value.decode('utf-8'))
    date_object = datetime.now()
    current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
    with open("../temporary/customerthreshold.json", 'r') as f:
        userinfo = json.load(f)
    categorythreshold = userinfo["usercategory"]
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    cur.execute("INSERT INTO customer(inserttime, anomalytype, userid, threshold, value) VALUES(%s, %s, %s, %s, %s)",
                (current_time, "category", newid, categorythreshold, newval))
    conn.commit()
    cur.close()
    conn.close()
            
