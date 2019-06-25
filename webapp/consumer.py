from kafka import KafkaConsumer
import numpy as np
import pickle


consumer = KafkaConsumer('heatmap',
                         bootstrap_servers=['localhost:9092'])
id = 0
for message in consumer:
    try:
        newmat = pickle.loads(message.value)
        # newmat.reshape((501,501))
        # print(id)
        # print(np.max(newmat))
        id += 1
        np.save('temporary/currentrain.npy', newmat)
    except:
        pass
