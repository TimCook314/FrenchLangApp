from collections import deque

class RunningAverage:
    def __init__(self, size):
        self.size = size
        self.queue = deque(maxlen=size)
        self.total = 0

    def add_value(self, value):
        if len(self.queue) == self.size:
            self.total -= self.queue[0]
        self.queue.append(value)
        self.total += value

    def get_average(self):
        if not self.queue:
            return 0
        return self.total / len(self.queue)

# Example usage:
# running_avg = RunningAverage(5)
# running_avg.add_value(10)
# running_avg.add_value(20)
# print(running_avg.get_average())  # Output will be the average of the values in the buffer

