CREATE INDEX IF NOT EXISTS when_sr_idx ON eg_sr_action ("when");
CREATE INDEX IF NOT EXISTS action_sr_idx ON eg_sr_action ("action");
CREATE INDEX IF NOT EXISTS servicerequestid_sr_idx ON eg_sr_action ("businesskey");
create sequence IF NOT EXISTS SEQ_EG_SR_SERVICEREQUESTID;