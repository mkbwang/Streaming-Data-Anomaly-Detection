from kafka import KafkaConsumer
import numpy as np
import pickle


consumer = KafkaConsumer('notification_job_0',
                         bootstrap_servers=['localhost:9092'])
id = 0
array = np.zeros((501,501))
mykey = ""
for message in consumer:
    newkey = message.key.decode('utf-8')
    timestamp = newkey.split('@')[1]
    newval = message.value.decode('utf-8')
    if mykey!=timestamp:
        np.save('temporary/example.npy', array)
        array = np.zeros((501,501))
        mykey = timestamp
        
    else:
        position = int(message.value.decode('utf-8'))
        array[position//501, position%501]=1
        # newmat.reshape((501,501))
        # print(message.key)
        # print(message.value)        
        # id += 1
        # np.save('temporary/currentrain.npy', newmat)
