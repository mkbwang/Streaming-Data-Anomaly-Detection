from kafka import KafkaProducer
from kafka.errors import KafkaError
import numpy as np
import pickle
import time


if __name__=="__main__":
    producer = KafkaProducer(bootstrap_servers=['localhost:9092'])

    id = 0
    while True:
        print(id)
        newfilename = "/mnt/matrices/array_" + str(id % 10000) + ".npy"
        newrain = np.load(newfilename)
        newrain = np.nan_to_num(newrain)
        newrain = newrain.flatten()
        # print(newrain.shape)
        msg = pickle.dumps(newrain.astype(np.uint8))
        # newrain = np.array([[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0]])
        future = producer.send("test", msg)
        time.sleep(0.5)
        id += 1
