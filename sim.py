# goal: prevent someone from falling asleep by measuring heart rate.
# if heart rate falls 10% below 3 minute moving average, send an alert
# keep a representative sample log of heart rate each hour throughout the day
# graph average heart rate throughout the day

import sys
import numpy as np
import random as rd
import time
import math
import matplotlib.pyplot as plt
#developer modified baselines
avgbpmbaseline = 70
beatintervalbaseline = 60.0/70
numbeats = 300

heartbeatsintervals = [] #y-axis



#create heart rate simluation 
for i in range(numbeats):
    currinterval = beatintervalbaseline * rd.uniform(0.8, 1.2)
    heartbeatsintervals.append(currinterval)


#print(heartbeatsintervals)

avgbpm = sum(heartbeatsintervals) / numbeats
print("average: " + str(avgbpm))


xaxis = range(numbeats)

plt.plot(xaxis, heartbeatsintervals)
plt.ylim(0, 2)
plt.show()


#traverse through
fivevalueaverage = []
for i in range(len(heartbeatsintervals)):
    fivevalueaverage.append(heartbeatsintervals[i])
    if len(fivevalueaverage) > 5:
        fivevalueaverage = fivevalueaverage[1:]
    if heartbeatsintervals[i] < 0.8 * sum(fivevalueaverage) / len(fivevalueaverage):
        print("WAKE UP")
        print(str(i) + " " + str(heartbeatsintervals[i]))
        break

