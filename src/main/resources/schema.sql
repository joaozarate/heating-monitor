CREATE TABLE if NOT EXISTS notification
(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    notification_status varchar(1),
    message varchar(255),
    code varchar(255),
    created_date   timestamp,
    last_modified_date timestamp,
    subscription UUID
);

CREATE TABLE if NOT EXISTS subscription
(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    active boolean,
    device UUID,
    created_date   timestamp,
    last_modified_date timestamp
);
