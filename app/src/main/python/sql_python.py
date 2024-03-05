import mysql.connector


def get_mysql_connection():
    # Replace these values with your MySQL server details
    host = ""
    user = ""
    password = ""
    database = ""

    try:
        # Establish a connection to the MySQL server
        connection = mysql.connector.connect(
            host=host,
            user=user,
            password=password,
            database=database
        )

        if connection.is_connected():
            print("Connected to MySQL database")
            return connection

    except mysql.connector.Error as e:
        print(f"Error: {e}")

    return None


def create_users_table(connection, first_name, second_roll, third_status, table):
    try:
        # Create a cursor to execute SQL queries
        cursor = connection.cursor()

        # SQL statement to create the 'users' table
        create_table_query = f"""
            CREATE TABLE IF NOT EXISTS {table} (
                user_id INT AUTO_INCREMENT PRIMARY KEY,
                Date DATE DEFAULT CURRENT_DATE,
                username VARCHAR(250) NOT NULL,
                Roll INT,
                user_status VARCHAR(20)
            )
        """

        # Execute the SQL statement to create the table
        cursor.execute(create_table_query)

        # SQL statement to insert data into the 'users' table
        insert_query = f"INSERT INTO {table} (username, Roll, user_status) VALUES (%s, %s, %s)"

        # Execute the SQL statement to insert data
        cursor.execute(insert_query, (first_name, second_roll, third_status))

        # Commit the changes
        connection.commit()

        print("Table 'users' created and data inserted successfully")

    except mysql.connector.Error as e:
        print(f"Error: {e}")

    finally:
        # Close the cursor and connection
        if 'cursor' in locals():
            cursor.close()


def final_value(first_name, second_roll, third_status, table):
    # Example usage:
    connection = get_mysql_connection()
    if connection:
        create_users_table(connection, first_name, second_roll, third_status, table)
        # Perform other database operations here if needed
        connection.close()  # Close the connection when done
        return "successfully"
    else:
        return "wrong"

def get_table_data_for_date(table_name, target_date):
    connection = get_mysql_connection()

    if connection:
        try:
            cursor = connection.cursor(dictionary=True)

            # Assuming your table has a date column named 'Date'
            query = f"SELECT * FROM {table_name} WHERE Date = %s"
            cursor.execute(query, (target_date,))

            # Fetch all rows from the table as dictionaries
            rows = cursor.fetchall()

            # Convert each dictionary to a list and remove the 'Date' column
            result = []
            for row in rows:
                row_list = list(row.values())
                row_list.remove(row['Date'])  # Remove the 'Date' column
                result.append(row_list)

            return result

        except mysql.connector.Error as e:
            return "wrong"

        finally:
            # Close cursor and connection
            cursor.close()
            connection.close()

def get_table_data_for_date_range(table_name, start_date, end_date):
    connection = get_mysql_connection()

    if connection:
        try:
            cursor = connection.cursor(dictionary=True)

            # Assuming your table has a date column named 'Date'
            query = f"SELECT * FROM {table_name} WHERE Date BETWEEN %s AND %s"
            cursor.execute(query, (start_date, end_date))

            # Fetch all rows from the table as dictionaries
            rows = cursor.fetchall()

            # Convert each dictionary to a list and remove the 'Date' column
            result = []
            for row in rows:
                row_list = list(row.values())
                row_list.remove(row['Date'])  # Remove the 'Date' column
                result.append(row_list)

            return result

        except mysql.connector.Error as e:
            return "wrong"

        finally:
            # Close cursor and connection
            cursor.close()
            connection.close()


def get_table_data_for_date_range_with_advanced(table_name, start_date, end_date):
    connection = get_mysql_connection()

    if connection:
        try:
            cursor = connection.cursor(dictionary=True)

            # Assuming your table has a date column named 'Date'
            query = f"SELECT * FROM {table_name} WHERE Date BETWEEN %s AND %s"
            cursor.execute(query, (start_date, end_date))

            # Fetch all rows from the table as dictionaries
            rows = cursor.fetchall()

            # Convert each dictionary to a list and remove the 'Date' column
            result = []
            for row in rows:
                # Format the date in the desired "YYYY-MM-DD" format
                formatted_date = row['Date'].strftime('%Y-%m-%d')
                row_list = list(row.values())
                row_list[row_list.index(row['Date'])] = formatted_date

                result.append(row_list)

            return result

        except mysql.connector.Error as e:
            return "wrong"

        finally:
            # Close cursor and connection
            cursor.close()
            connection.close()
