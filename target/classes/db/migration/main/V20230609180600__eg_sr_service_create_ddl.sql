CREATE TABLE IF NOT EXISTS eg_sr_service
(
  tenantid character varying(64) NOT NULL,
  servicecode character varying(128) NOT NULL,
  servicerequestid character varying(64) NOT NULL,
  description character varying(500),
  lat numeric(9,6),
  "long" numeric(10,7),
  address character varying(256),
  addressid character varying(64),
  email character varying(256),
  deviceid character varying(64),
  accountid character varying(64),
  firstname character varying(32),
  lastname character varying(64),
  phone character varying(64) NOT NULL,
  attributes jsonb,
  status character varying(64),
  source character varying(64),
  expectedtime bigint,
  feedback character varying(500),
  rating character varying(5),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  slaendtime bigint,
  landmark character varying(2000),
  active boolean DEFAULT true,
  usertype character varying(256),
  service character varying(256),
  priority character varying(256),
  assignee character varying(256),
  impact character varying(256),
  CONSTRAINT pk_eg_sr_service PRIMARY KEY (tenantid, servicerequestid)
);



CREATE TABLE IF NOT EXISTS eg_sr_action
(
  uuid character varying(64),
  tenantid character varying(256) NOT NULL,
  by character varying(256) ,
  isinternal boolean,
  "when" bigint NOT NULL,
  businesskey character varying(500) NOT NULL,
  status character varying(64),
  assignee character varying(256),
  media JSONB,
  comments character varying(1024),
  "action" character varying(64),
  CONSTRAINT pk_eg_sr_action PRIMARY KEY (uuid, tenantid)
);


CREATE TABLE IF NOT EXISTS eg_sr_address(

  uuid varchar(256),
  mohalla varchar(256),
  landmark varchar(256),
  latitude numeric(9,6),
  longitude numeric(10,7),
  city varchar(256),
  tenantid varchar(256),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  pincode character varying(64),
  housenoandstreetname character varying(512),
  CONSTRAINT pk_eg_sr_address PRIMARY KEY (tenantId,uuid)
); 

