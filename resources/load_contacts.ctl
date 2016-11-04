load data
 infile '/home/oracle/t.txt'
 into table dg_re_targetlist
 fields terminated by "|"
 ( version, id, campaign_id, contact_id, email_address, salutation, prefix, first_name, middle_name, last_name,
suffix, salesrep_id, salesaccount_id, salesaccount_name, salesaccount_description, ok_to_contact, ok_to_email,
current_component_id, current_component_name, current_state, current_score, last_action_date, component_score )
