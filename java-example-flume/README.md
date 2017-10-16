# Java - Flume

`bin/flume-ng agent --name agent --conf-file conf/flume-conf.properties`

```text
agent.sources = example
agent.channels = memory
agent.sinks = file

agent.sources.example.type = avro
agent.sources.example.bind = 0.0.0.0
agent.sources.example.port = 5555
agent.sources.example.channels = memory

agent.sinks.file.type = file_roll
agent.sinks.file.sink.directory = /var/flume
agent.sinks.file.channel = memory

agent.channels.memory.type = memory
agent.channels.memory.capacity = 100
```

*PS：本文使用的是flume-1.6.0*