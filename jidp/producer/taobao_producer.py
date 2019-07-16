from kafka import KafkaProducer
from kafka.errors import KafkaError
import numpy as np
import pickle
import threading
import time
import json
import csv


def csv_reader(name):
    producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                             bootstrap_servers='localhost:9092', linger_ms=10)
    line = 0
    in_csv = open("/mnt/taobao/" + name + ".csv")
    reader = csv.reader(in_csv)
    for in_line in reader:
        if line > 40000:
            in_csv.seek(1)
            line=1
        if line == 0:
            line += 1
            continue
        future = producer.send(name+"_user", key=bytes(str(in_line[2]),encoding="utf-8"), value=str(in_line[1]))
        future = producer.send("user_"+name, key=bytes(str(in_line[1]),encoding="utf-8"), value=str(in_line[2]))
        producer.flush()
        time.sleep(0.02)
        line += 1


if __name__ == "__main__":
    print("Successfully create producer")
    x_brand = threading.Thread(target=csv_reader, args=('brand',))
    x_product = threading.Thread(target=csv_reader, args=('product',))
    x_category = threading.Thread(target=csv_reader, args=('category',))
    x_brand.start()
    x_product.start()
    x_category.start()
    x_brand.join()
    x_product.join()
    x_category.join()
