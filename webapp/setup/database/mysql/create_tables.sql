create table CMS_USERS                  (USER_ID VARCHAR(36) BINARY NOT NULL,
                                         USER_NAME VARCHAR(64) BINARY NOT NULL,
                                         USER_PASSWORD VARCHAR(32) BINARY NOT NULL,
                                         USER_RECOVERY_PASSWORD VARCHAR(32) BINARY NOT NULL,
                                         USER_DESCRIPTION VARCHAR(255) NOT NULL,
                                         USER_FIRSTNAME VARCHAR(50) NOT NULL,
                                         USER_LASTNAME VARCHAR(50) NOT NULL,
                                         USER_EMAIL VARCHAR(100) NOT NULL,
                                         USER_LASTLOGIN DATETIME NOT NULL,
                                         USER_LASTUSED DATETIME NOT NULL,
                                         USER_FLAGS INT NOT NULL,
                                         USER_INFO blob,
                                         USER_DEFAULT_GROUP_ID VARCHAR(36) BINARY NOT NULL,
                                         USER_ADDRESS VARCHAR(100) NOT NULL,
                                         USER_SECTION VARCHAR(50) NOT NULL,
                                         USER_TYPE INT NOT NULL,
                                         primary key(USER_ID), unique(USER_NAME));

create table CMS_GROUPS                 (GROUP_ID VARCHAR(36) BINARY NOT NULL,
                                         PARENT_GROUP_ID VARCHAR(36) BINARY NOT NULL,
                                         GROUP_NAME VARCHAR(64) BINARY NOT NULL,
                                         GROUP_DESCRIPTION VARCHAR(255) NOT NULL,
                                         GROUP_FLAGS INT NOT NULL,
                                         primary key(GROUP_ID),
                                         unique(GROUP_NAME),
                                         key group_parentid (parent_group_id));

create table CMS_GROUPUSERS             (GROUP_ID VARCHAR(36) BINARY NOT NULL,
                                         USER_ID VARCHAR(36) BINARY NOT NULL,
                                         GROUPUSER_FLAGS INT NOT NULL,
                                         key(GROUP_ID),
                                         key(USER_ID));

create table CMS_PROJECTS               (PROJECT_ID INT NOT NULL, 
                                         USER_ID VARCHAR(36) BINARY NOT NULL,
                                         GROUP_ID VARCHAR(36) BINARY NOT NULL, 
                                         MANAGERGROUP_ID VARCHAR(36) BINARY NOT NULL,
                                         TASK_ID INT NOT NULL,
                                         PROJECT_NAME VARCHAR(64) BINARY NOT NULL,
                                         PROJECT_DESCRIPTION VARCHAR(255) NOT NULL,
                                         PROJECT_FLAGS INT NOT NULL,
                                         PROJECT_CREATEDATE DATETIME NOT NULL,
                                         PROJECT_TYPE INT NOT NULL,
                                         primary key(PROJECT_ID), 
                                         key(PROJECT_NAME, PROJECT_CREATEDATE),
                                         key project_flags (project_flags),
                                         key projects_groupid (group_id),
                                         key projects_managerid (managergroup_id),
                                         key projects_userid (user_id),
                                         key projects_taskid (task_id),
                                         unique(PROJECT_NAME, PROJECT_CREATEDATE));

create table CMS_BACKUP_PROJECTS         (TAG_ID INT NOT NULL,
                                          PROJECT_ID INT NOT NULL,
                                          PROJECT_NAME VARCHAR(64) BINARY NOT NULL,
                                          PROJECT_PUBLISHDATE DATETIME,
                                          PROJECT_PUBLISHED_BY VARCHAR(36) BINARY NOT NULL,
                                          PROJECT_PUBLISHED_BY_NAME VARCHAR(167),
                                          USER_ID VARCHAR(36) BINARY NOT NULL,
                                          USER_NAME VARCHAR(167),
                                          GROUP_ID VARCHAR(36) BINARY NOT NULL,
                                          GROUP_NAME VARCHAR(64) BINARY,
                                          MANAGERGROUP_ID VARCHAR(36) BINARY NOT NULL,
                                          MANAGERGROUP_NAME VARCHAR(64) BINARY,
                                          PROJECT_DESCRIPTION VARCHAR(255) NOT NULL,
                                          PROJECT_CREATEDATE DATETIME NOT NULL,
                                          PROJECT_TYPE INT NOT NULL,
                                          TASK_ID INT NOT NULL,
                                          primary key (TAG_ID));

create table CMS_PROJECTRESOURCES       (PROJECT_ID INT NOT NULL,
                                         RESOURCE_PATH VARCHAR(248) NOT NULL,
                                         primary key(PROJECT_ID, RESOURCE_PATH),
                                         index projectresource_resource_path (RESOURCE_PATH));

create table CMS_BACKUP_PROJECTRESOURCES (TAG_ID INT NOT NULL,
                                          PROJECT_ID INT NOT NULL,
                                          RESOURCE_PATH VARCHAR(248) NOT NULL,
                                          primary key (TAG_ID, PROJECT_ID, RESOURCE_PATH));


create table CMS_OFFLINE_PROPERTYDEF     (PROPERTYDEF_ID INT NOT NULL, 
                                         PROPERTYDEF_NAME VARCHAR(128) BINARY NOT NULL,
                                         PROPERTYDEF_MAPPING_TYPE INT NOT NULL,
                                         primary key(PROPERTYDEF_ID), 
                                         unique(PROPERTYDEF_NAME, PROPERTYDEF_MAPPING_TYPE));

create table CMS_ONLINE_PROPERTYDEF      (PROPERTYDEF_ID INT NOT NULL, 
                                         PROPERTYDEF_NAME VARCHAR(128) BINARY NOT NULL,
                                         PROPERTYDEF_MAPPING_TYPE INT NOT NULL,
                                         primary key(PROPERTYDEF_ID), 
                                         unique(PROPERTYDEF_NAME, PROPERTYDEF_MAPPING_TYPE));
                                        
create table CMS_BACKUP_PROPERTYDEF      (PROPERTYDEF_ID INT NOT NULL, 
                                         PROPERTYDEF_NAME VARCHAR(128) BINARY NOT NULL,
                                         PROPERTYDEF_MAPPING_TYPE INT NOT NULL,
                                         primary key(PROPERTYDEF_ID), 
                                         unique(PROPERTYDEF_NAME, PROPERTYDEF_MAPPING_TYPE));

create table CMS_OFFLINE_PROPERTIES     (PROPERTY_ID INT NOT NULL,
                                         PROPERTYDEF_ID INT NOT NULL,
                                         PROPERTY_MAPPING_ID VARCHAR(36) NOT NULL,
                                         PROPERTY_MAPPING_TYPE INT NOT NULL,
                                         PROPERTY_VALUE TEXT NOT NULL,
                                         primary key(PROPERTY_ID));
                                         

create table CMS_ONLINE_PROPERTIES      (PROPERTY_ID INT NOT NULL,
                                         PROPERTYDEF_ID INT NOT NULL,
                                         PROPERTY_MAPPING_ID VARCHAR(36) NOT NULL,
                                         PROPERTY_MAPPING_TYPE INT NOT NULL,
                                         PROPERTY_VALUE TEXT NOT NULL,
                                         primary key(PROPERTY_ID));
                                        
                                         
create table CMS_BACKUP_PROPERTIES      (BACKUP_ID VARCHAR(36) BINARY NOT NULL,
										 PROPERTY_ID INT NOT NULL,
                                         PROPERTYDEF_ID INT NOT NULL,
                                         PROPERTY_MAPPING_ID VARCHAR(36) NOT NULL,
                                         PROPERTY_MAPPING_TYPE INT NOT NULL,
                                         PROPERTY_VALUE TEXT NOT NULL,
                                         TAG_ID INT,
                                         VERSION_ID	INT NOT NULL,
                                         primary key(PROPERTY_ID)); 
                                     
                                         
create table CMS_OFFLINE_RESOURCES      (RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
                                         RESOURCE_TYPE INT NOT NULL,
                                         RESOURCE_FLAGS INT NOT NULL,
                                         DATE_CREATED BIGINT NOT NULL,
                                         DATE_LASTMODIFIED BIGINT NOT NULL,
										 USER_CREATED VARCHAR(36) BINARY NOT NULL,                                         
                                         USER_LASTMODIFIED VARCHAR(36) BINARY NOT NULL,
										 PROJECT_ID SMALLINT UNSIGNED NOT NULL,
                                         RESOURCE_STATE	SMALLINT UNSIGNED NOT NULL,
                                         RESOURCE_SIZE INT NOT NULL,                                         
                                         LINK_COUNT INT NOT NULL,                                         
                                         primary key(RESOURCE_ID),
                                         key resources_type (RESOURCE_TYPE));

create table CMS_ONLINE_RESOURCES       (RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
                                         RESOURCE_TYPE INT NOT NULL,
                                         RESOURCE_FLAGS INT NOT NULL,
                                         DATE_CREATED BIGINT NOT NULL,
                                         DATE_LASTMODIFIED BIGINT NOT NULL,
                                         USER_CREATED VARCHAR(36) BINARY NOT NULL,                                         
                                         USER_LASTMODIFIED VARCHAR(36) BINARY NOT NULL,
                                         PROJECT_ID SMALLINT UNSIGNED NOT NULL,
                                         RESOURCE_STATE	SMALLINT UNSIGNED NOT NULL,
                                         RESOURCE_SIZE INT NOT NULL,
                                         LINK_COUNT INT NOT NULL,
                                         primary key(RESOURCE_ID),
                                         key resources_type (RESOURCE_TYPE));
                                         
create table CMS_BACKUP_RESOURCES       (BACKUP_ID VARCHAR(36) BINARY NOT NULL,
										 RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
                                         RESOURCE_TYPE INT NOT NULL,
                                         RESOURCE_FLAGS INT NOT NULL,
                                         ACCESS_FLAGS INT NOT NULL,
                                         DATE_CREATED BIGINT NOT NULL,
                                         DATE_LASTMODIFIED BIGINT NOT NULL,
                                         USER_CREATED VARCHAR(36) BINARY NOT NULL,
                                         USER_LASTMODIFIED VARCHAR(36) BINARY NOT NULL,
                                         PROJECT_ID SMALLINT UNSIGNED NOT NULL,
                                         RESOURCE_STATE	SMALLINT UNSIGNED NOT NULL,
                                         RESOURCE_SIZE INT NOT NULL,
                                         LINK_COUNT INT NOT NULL,
                                         TAG_ID INT NOT NULL,
                                         VERSION_ID	INT NOT NULL,
                                         USER_CREATED_NAME VARCHAR(64) NOT NULL,
                                         USER_LASTMODIFIED_NAME VARCHAR(64) NOT NULL,
                                         primary key(BACKUP_ID),
                                         key resource_resourceid (RESOURCE_ID),
                                         key resources_type (RESOURCE_TYPE));
                                         
create table CMS_OFFLINE_CONTENTS          (CONTENT_ID VARCHAR(36) BINARY NOT NULL,
										 RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
                                         FILE_CONTENT MEDIUMBLOB NOT NULL,
                                         primary key(CONTENT_ID));

create table CMS_ONLINE_CONTENTS           (CONTENT_ID VARCHAR(36) BINARY NOT NULL,
										 RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
                                         FILE_CONTENT MEDIUMBLOB NOT NULL,
                                         primary key(CONTENT_ID));

create table CMS_BACKUP_CONTENTS           (BACKUP_ID VARCHAR(36) BINARY NOT NULL,
										 CONTENT_ID VARCHAR(36) BINARY NOT NULL,
										 RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
                                         FILE_CONTENT MEDIUMBLOB NOT NULL,
                                         TAG_ID INT,
                                         VERSION_ID	INT NOT NULL,
                                         primary key(BACKUP_ID));

create table CMS_SYSTEMID               (TABLE_KEY VARCHAR(255) NOT NULL,
                                         ID INT NOT NULL,
                                         primary key(TABLE_KEY));

create table CMS_EXPORT_DEPENDENCIES    (LINK_ID INT NOT NULL,
                                         RESOURCENAME VARCHAR(255),
                                         key (LINK_ID),
                                         unique(LINK_ID, RESOURCENAME));
                                                                                 
CREATE TABLE CMS_TASK 					(AUTOFINISH INT(11),
                                           ENDTIME DATETIME,
                                           ESCALATIONTYPEREF INT(11),
                                           ID INT(11) NOT NULL,
                                           INITIATORUSERREF VARCHAR(36) BINARY,
                                           MILESTONEREF INT(11),
                                           NAME VARCHAR(254),
                                           ORIGINALUSERREF VARCHAR(36) BINARY,
                                           AGENTUSERREF VARCHAR(36) BINARY,
                                           PARENT INT(11),
                                           PERCENTAGE VARCHAR(50),
                                           PERMISSION VARCHAR(50),
                                           PRIORITYREF INT(11) DEFAULT '2',
                                           ROLEREF VARCHAR(36) BINARY,
                                           ROOT INT(11),
                                           STARTTIME DATETIME,
                                           STATE INT(11),
                                           TASKTYPEREF INT(11),
                                           TIMEOUT DATETIME,
                                           WAKEUPTIME DATETIME,
                                           HTMLLINK VARCHAR(254),
                                           ESTIMATETIME INT(11) DEFAULT '86400',
                                           primary key (id)
                                         );
                                                                                 
CREATE TABLE CMS_TASKTYPE 				(AUTOFINISH INT(11),
                                           ESCALATIONTYPEREF INT(11),
                                           HTMLLINK VARCHAR(254),
                                           ID INT(11) NOT NULL,
                                           NAME VARCHAR(50),
                                           PERMISSION VARCHAR(50),
                                           PRIORITYREF INT(11),
                                           ROLEREF VARCHAR(36) BINARY,
                                           primary key (id)
                                         );
                                                                                 
CREATE TABLE CMS_TASKLOG 				(COMENT text,
                                           EXTERNALUSERNAME VARCHAR(254),
                                           ID INT(11) NOT NULL,
                                           STARTTIME DATETIME,
                                           TASKREF INT(11),
                                           USERREF VARCHAR(36) BINARY NOT NULL,
                                           TYPE INT(18) DEFAULT '0',
                                           primary key (id)
                                         );
                                         
CREATE TABLE CMS_TASKPAR 				(ID INT(11) NOT NULL ,
                                           PARNAME VARCHAR(50),
                                           PARVALUE VARCHAR(50),
                                           REF INT(11),
                                           primary key (id)
                                         );
                                         
create table CMS_WEBUSERS
(  USER_ID                  VARCHAR(36) BINARY NOT NULL,
   USER_MEMBER_ID           VARCHAR(255),
   USER_SALUTATION          VARCHAR(255),
   USER_TITLE               VARCHAR(255),
   USER_PWD		            VARCHAR(255),   
   USER_PWD_QUESTION        VARCHAR(255),   
   USER_PWD_ANSWER          VARCHAR(255),        
   USER_CITY                VARCHAR(255),
   USER_POSTCODE            VARCHAR(255),
   USER_STATE               VARCHAR(255),
   USER_COUNTRY             VARCHAR(255),
   USER_ADDRESS_TYPE        INT,
   USER_BIRTHDAY            DATETIME,
   USER_PHONE               VARCHAR(255),
   USER_FAX                 VARCHAR(255),
   USER_MOBILE              VARCHAR(255),
   USER_ACCEPT              INT,   
   USER_RECOMMENDED_BY	    VARCHAR(255), 
   USER_PROFESSION          VARCHAR(255),
   USER_COMPANY             VARCHAR(255),
   USER_DEPARTMENT	        VARCHAR(255),
   USER_POSITION            VARCHAR(255),         
   USER_ACCOUNT_NUMBER      VARCHAR(255),
   USER_BANK_NUMBER         VARCHAR(255),
   USER_BANK                VARCHAR(255),
   USER_NEWSLETTER          VARCHAR(255),   
   USER_EXTRAINFO_1         VARCHAR(255),
   USER_EXTRAINFO_2         VARCHAR(255),
   USER_EXTRAINFO_3         VARCHAR(255),
   USER_EXTRAINFO_4         VARCHAR(255),
   USER_EXTRAINFO_5         VARCHAR(255),
   USER_EXTRAINFO_6         VARCHAR(255),
   USER_EXTRAINFO_7         VARCHAR(255),
   USER_EXTRAINFO_8         VARCHAR(255),
   USER_EXTRAINFO_9         VARCHAR(255),
   USER_EXTRAINFO_10        VARCHAR(255),
   USER_EXTRAINFO_11        VARCHAR(255),
   USER_EXTRAINFO_12        VARCHAR(255),
   USER_EXTRAINFO_13        VARCHAR(255),
   USER_EXTRAINFO_14        VARCHAR(255),
   USER_EXTRAINFO_15        VARCHAR(255),
   USER_EXTRAINFO_16        VARCHAR(255),
   USER_EXTRAINFO_17        VARCHAR(255),
   USER_EXTRAINFO_18        VARCHAR(255),
   USER_EXTRAINFO_19        TEXT,
   USER_EXTRAINFO_20        TEXT,
   USER_PICTURE		    	MEDIUMBLOB,
   USER_PICTURE_NAME	    VARCHAR(255),         
   USER_CREATE_DATE         DATETIME,
   USER_LASTCHANGE_BY       VARCHAR(255),
   USER_LASTCHANGE_DATE     DATETIME,
   LOCKSTATE                INT,
   primary key (USER_ID)
);

create table CMS_ONLINE_ACCESSCONTROL
(	RESOURCE_ID				VARCHAR(36) BINARY NOT NULL,
	PRINCIPAL_ID			VARCHAR(36) BINARY NOT NULL,
	ACCESS_ALLOWED			INT,
	ACCESS_DENIED			INT,
	ACCESS_FLAGS			INT,
	primary key				(RESOURCE_ID, PRINCIPAL_ID)
);

create table CMS_OFFLINE_ACCESSCONTROL
(	RESOURCE_ID				VARCHAR(36) BINARY NOT NULL,
	PRINCIPAL_ID			VARCHAR(36) BINARY NOT NULL,
	ACCESS_ALLOWED			INT,
	ACCESS_DENIED			INT,
	ACCESS_FLAGS			INT,
	primary key				(RESOURCE_ID, PRINCIPAL_ID)
);

CREATE TABLE CMS_OFFLINE_STRUCTURE (
	STRUCTURE_ID			VARCHAR(36) BINARY NOT NULL,
	PARENT_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH			TEXT NOT NULL,
	STRUCTURE_STATE			SMALLINT UNSIGNED NOT NULL,
	DATE_RELEASED 			BIGINT NOT NULL,
	DATE_EXPIRED 			BIGINT NOT NULL,
	primary key				(STRUCTURE_ID),
	INDEX IDX1 				(STRUCTURE_ID, RESOURCE_PATH(255)),
	INDEX IDX2 				(RESOURCE_PATH(255), RESOURCE_ID),
	INDEX IDX3 				(STRUCTURE_ID, PARENT_ID),
	INDEX IDX4 				(STRUCTURE_ID, RESOURCE_ID),
	INDEX IDX7				(STRUCTURE_STATE),
	INDEX IDX8 				(PARENT_ID)
);

CREATE TABLE CMS_ONLINE_STRUCTURE (
	STRUCTURE_ID			VARCHAR(36) BINARY NOT NULL,
	PARENT_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH			TEXT NOT NULL,
	STRUCTURE_STATE 		SMALLINT UNSIGNED NOT NULL,
	DATE_RELEASED 			BIGINT NOT NULL,
	DATE_EXPIRED 			BIGINT NOT NULL,
	primary key				(STRUCTURE_ID),
	INDEX IDX1 				(STRUCTURE_ID, RESOURCE_PATH(255)),
	INDEX IDX2 				(RESOURCE_PATH(255), RESOURCE_ID),
	INDEX IDX3 				(STRUCTURE_ID, PARENT_ID),
	INDEX IDX4 				(STRUCTURE_ID, RESOURCE_ID),
	INDEX IDX7				(STRUCTURE_STATE),
	INDEX IDX8 				(PARENT_ID)
);

CREATE TABLE CMS_BACKUP_STRUCTURE (
	BACKUP_ID 				VARCHAR(36) BINARY 	NOT NULL,
    TAG_ID					INT NOT NULL,
    VERSION_ID				INT NOT NULL,
	STRUCTURE_ID			VARCHAR(36) BINARY NOT NULL,
	PARENT_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH			TEXT NOT NULL,
	STRUCTURE_STATE			SMALLINT UNSIGNED NOT NULL,
	DATE_RELEASED 			BIGINT NOT NULL,
	DATE_EXPIRED 			BIGINT NOT NULL,
	primary key				(BACKUP_ID),
	INDEX IDX1 				(STRUCTURE_ID, RESOURCE_PATH(255)),
	INDEX IDX2 				(RESOURCE_PATH(255), RESOURCE_ID),
	INDEX IDX3 				(STRUCTURE_ID, PARENT_ID),
	INDEX IDX4 				(STRUCTURE_ID, RESOURCE_ID),
	INDEX IDX7				(STRUCTURE_STATE),
	INDEX IDX8				(TAG_ID)
);

CREATE TABLE CMS_PUBLISH_HISTORY (
	PUBLISH_ID					VARCHAR(36) BINARY NOT NULL,
	TAG_ID						INT NOT NULL,
	STRUCTURE_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID					VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH				TEXT NOT NULL,
	RESOURCE_STATE				INT NOT NULL,
	RESOURCE_TYPE				INT NOT NULL,
	SIBLING_COUNT				INT NOT NULL,
	MASTER_ID					VARCHAR(36) BINARY NOT NULL,
	CONTENT_DEFINITION_NAME		VARCHAR(128),
	primary key					(PUBLISH_ID, TAG_ID, STRUCTURE_ID, MASTER_ID)
);

CREATE TABLE CMS_PUBLISHED_RESOURCES (
	PUBLISHED_ID				VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH				TEXT NOT NULL,
	LINK_TYPE					INT NOT NULL,
	LINK_PARAMETER				TEXT,
	LINK_TIMESTAMP				BIGINT,	
	primary key					(PUBLISHED_ID)	
);


