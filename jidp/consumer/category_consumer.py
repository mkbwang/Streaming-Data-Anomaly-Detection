import psycopg2 as pg
from kafka import KafkaConsumer
from datetime import datetime
import json

consumer = KafkaConsumer('notification_job_0',
                         bootstrap_servers=['localhost:9092'])

for message in consumer:
    newid = message.value.decode('utf-8')
    date_object = datetime.now()
    current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
    with open("../temporary/othersthreshold.json", 'r') as f:
        othersinfo = json.load(f)
    categorythreshold = othersinfo["category"]
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    cur.execute("INSERT INTO others(inserttime, anomalytype, id, threshold) VALUES(%s, %s, %s, %s)",
                (current_time, "category", newid, categorythreshold))
    conn.commit()
    cur.close()
    conn.close()
            
