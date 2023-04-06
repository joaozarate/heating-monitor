CREATE TABLE if NOT EXISTS notification
(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    notification_status varchar(1),
    message varchar(255),
    code varchar(255),
    created_date   timestamp,
    last_modified_date timestamp,
    subscription_id UUID
);

CREATE TABLE if NOT EXISTS subscription
(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    base_receiver_url varchar(255),
    relative_receiver_url varchar(255),
    active varchar(255),
    event varchar(255),
    appliance varchar(255),
    created_date   timestamp,
    last_modified_date timestamp
);
