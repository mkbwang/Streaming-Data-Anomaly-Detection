"""This script is meant to illustrate how to connect to posgreSQL."""
import psycopg2 as pg
import numpy as np
from datetime import datetime
import time

if __name__=="__main__":
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor() # connect to the server and get the cursor
    for ts in range(10):
        date_object = datetime.now()
        current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
        rainpoint = [100,100]
        outlierpoint = [400,100]
        cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points, threshold, value) VALUES(%s, %s, %s ,%s, %s)",
                    (current_time, "threshold", rainpoint, 8, 20)) # store data into anomalydb
        cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points, value) VALUES(%s, %s, %s, %s)",
                    (current_time, "statistical", outlierpoint, 20)) # store data into anomalydb
        cur.execute("INSERT INTO customer (inserttime, anomalytype, userid, threshold, value) VALUES(%s, %s, %s ,%s, %s)",
                    (current_time, "brand", "89715", 8, 14))
        cur.execute("INSERT INTO others (inserttime, anomalytype, id, threshold, value) VALUES(%s, %s, %s ,%s, %s)",
                    (current_time, "category", "3346", 8, 14))
        time.sleep(0.5)
    conn.commit()
    # an example of selecting certain rows ordered by time of insertion
    # cur.execute("SELECT inserttime FROM rainfall ORDER BY inserttime DESC LIMIT 2 ;")
    # mytime = cur.fetchall()
    # processedtime = [tt[0].strftime("%H:%M:%S") for tt in mytime]
    # print(processedtime)
    cur.close()
    conn.close()
