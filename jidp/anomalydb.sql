CREATE TABLE rainfall (
    inserttime            timestamp,
    anomalytype         varchar(80),           
    points         int[][],           
    threshold       int
);

CREATE TABLE customer (
    inserttime            timestamp,
    anomalytype         varchar(80),           
    userid          varchar(80),           
    threshold       int
);

CREATE TABLE others(
    inserttime          timestamp,
    anomalytype         varchar(80),
    id                  varchar(80),
    threshold           float
);
