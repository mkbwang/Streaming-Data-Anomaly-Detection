"""This script is meant to illustrate how to connect to posgreSQL."""
import psycopg2 as pg
import numpy as np
from datetime import datetime
import time

if __name__=="__main__":
    conn = pg.connect("dbname=anomalydb user=wangmk")
    cur = conn.cursor() # connect to the server and get the cursor
    for ts in range(10):
        date_object = datetime.now()
        current_time = date_object.strftime('%Y-%m-%d %H:%M:%S')
        rainpoint = [[100,100],[400,400]]
        outlierpoint = [[400,100],[100,400]]
        cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points, threshold) VALUES(%s, %s, %s ,%s)",
                    (current_time, "threshold", rainpoint, 40)) # store data into anomalydb
        cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points) VALUES(%s, %s, %s)",
                    (current_time, "statistical", outlierpoint)) # store data into anomalydb
        cur.execute("INSERT INTO customer (inserttime, anomalytype, userid, threshold) VALUES(%s, %s, %s ,%s)",
                    (current_time, "brand", "89715", 14))
        cur.execute("INSERT INTO others (inserttime, anomalytype, id, threshold) VALUES(%s, %s, %s ,%s)",
                    (current_time, "category", "3346", 10))
        time.sleep(0.5)
    conn.commit()
    # an example of selecting certain rows ordered by time of insertion
    # cur.execute("SELECT inserttime FROM rainfall ORDER BY inserttime DESC LIMIT 2 ;")
    # mytime = cur.fetchall()
    # processedtime = [tt[0].strftime("%H:%M:%S") for tt in mytime]
    # print(processedtime)
    cur.close()
    conn.close()
