from kafka import KafkaConsumer
import numpy as np
import pickle
import json


consumer = KafkaConsumer('weatherdata',
                         bootstrap_servers=['localhost:9092'])
id = 0
array = np.zeros((100,100))
for message in consumer:
    # print("message received")
    newval = np.array(json.loads(message.value.decode('utf-8')))
    newval = newval.reshape((100, 100))
    np.save('../temporary/currentrain.npy', newval)

