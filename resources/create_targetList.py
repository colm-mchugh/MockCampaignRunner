#!/usr/bin/python

import sys, getopt

def main(argv):
    total_contacts = 0
    output_file = ' '

    try:
        opts, args = getopt.getopt(argv,"hc:o:", ["tgtListCnt=", "ofile="])
    except getopt.GetoptError:
        print 'create_targetList -c <tgt list count> -o <outputfile>'
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            print 'create_targetList -c <tgt list count> -o <outputfile>'
            sys.exit()
        elif opt in ("-c", "--tgtListCnt"):
            total_contacts = int(arg)
        elif opt in ("-o", "--ofile"):
            output_file = arg
        else:
            print 'Error. Proper usage: create_targetList -c <tgt list count> -o <outputfile>'
            sys.exit()

    print 'tgt list count = ', total_contacts
    print 'output file = ', output_file

    text_file = open(output_file, "w")
    for n in range(0, total_contacts):
        version = 0
        id = n
        campaign_id = 1
        contact_id = 1000 + n
        email_address = 'contact' + str(contact_id) + '@companyFoo.com' 
        salutation = 'hello' 

        modX = n % 4;
        if modX == 0:
            prefix = 'Mr'
        elif modX == 1:
            prefix = 'Mrs'
        elif modX == 2:
            prefix = 'Miss'
        elif modX == 3:
            prefix = 'Sir'
        else:
            print 'impossible mod result = ', modX
            exit()

        first_name = 'fname' + str(contact_id)
        middle_name = 'X'
        last_name = 'lname' + str(contact_id)
        suffix = 'N'
        salesrep_id = 'sid' + str(contact_id)
        salesaccount_id = n
        salesaccount_name = 'acctName' + str(contact_id)
        salesaccount_description = 'desc' + str(contact_id)
        ok_to_contact = 'true'
        ok_to_email = 'true'
        current_component_id = 0
        current_component_name = 'White Paper' 
        current_state = 0
        current_score = 0
        last_action_date = '02-NOV-16 12.44.54.000000000 PM'
        component_score = 0

        foo = str(version) + '|' + str(id) + '|' + str(campaign_id) + '|' + str(contact_id) + '|' + email_address + '|' + salutation + '|' + prefix \
            + '|' + first_name + '|' + middle_name + '|' + last_name + '|' + suffix + '|' + salesrep_id + '|' + str(salesaccount_id) + '|'          \
            + salesaccount_name + '|' + salesaccount_description + '|' + ok_to_contact + '|' + ok_to_email + '|' + str(current_component_id) + '|'  \
            + current_component_name + '|' + str(current_state) + '|' + str(current_score) + '|' + last_action_date + '|' + str(component_score) 

        text_file.write(foo + '\n')

        if n % 10000 == 0:
            print '# of records written: ', n 

    text_file.close() 

if __name__ == "__main__":
    main(sys.argv[1:])

