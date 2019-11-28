select *
from BATCH_STEP_EXECUTION
where JOB_EXECUTION_ID = '12';

select a.JOB_EXECUTION_ID,
       a.VERSION,
       a.JOB_INSTANCE_ID,
       a.CREATE_TIME,
       a.START_TIME,
       a.END_TIME,
       a.STATUS,
       a.EXIT_CODE,
       a.EXIT_MESSAGE,
       a.LAST_UPDATED,
       a.JOB_CONFIGURATION_LOCATION,
       b.JOB_NAME
from BATCH_JOB_EXECUTION a
         left join BATCH_JOB_INSTANCE b
                   on a.JOB_INSTANCE_ID = b.JOB_INSTANCE_ID
where a.create_time between '2019-08-20 00:00:00' and '2019-08-20 23:59:59'
ORDER BY a.CREATE_TIME desc;