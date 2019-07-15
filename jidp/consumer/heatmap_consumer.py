from kafka import KafkaConsumer
import numpy as np
import pickle
import json


consumer = KafkaConsumer('test1',
                         bootstrap_servers=['localhost:9092'])
id = 0
array = np.zeros((501,501))
for message in consumer:
    newval = np.array(json.loads(message.value.decode('utf-8')))
    np.save('temporary/currentrain.npy', newval)

