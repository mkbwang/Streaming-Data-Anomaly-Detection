from kafka import KafkaConsumer
import numpy as np
import pickle
import json


consumer = KafkaConsumer('weatherdata',
                         bootstrap_servers=['localhost:9092'])
id = 0
array = np.zeros((501,501))
for message in consumer:
    # print("message received")
    newval = np.array(json.loads(message.value.decode('utf-8')))
    newval = newval.reshape((501, 501))
    np.save('../temporary/currentrain.npy', newval)

