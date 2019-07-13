"""This script is meant to illustrate how to connect to posgreSQL."""
import psycopg2 as pg
import numpy as np
from datetime import datetime

if __name__=="__main__":
    conn = pg.connect("dbname=anomalydb user=wangmk")
    cur = conn.cursor() # connect to the server and get the cursor
    date_object = datetime.now()
    current_time = date_object.strftime('%H:%M:%S')
    anomalypoint = [[100,100],[400,400]]
    cur.execute("INSERT INTO rainfall (inserttime, anomalytype, points, threshold) VALUES(%s, %s, %s ,%s)",
                (current_time, "threshold", anomalypoint, 40)) # store data into anomalydb
    
    conn.commit()
    # an example of selecting certain rows ordered by time of insertion
    cur.execute("SELECT inserttime FROM rainfall ORDER BY inserttime DESC LIMIT 2 ;")
    mytime = cur.fetchall()
    processedtime = [tt[0].strftime("%H:%M:%S") for tt in mytime]
    print(processedtime)
    cur.close()
    conn.close()
