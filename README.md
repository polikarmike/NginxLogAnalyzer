# NGINX Log Analyzer

NGINX Log Analyzer is a tool for analyzing NGINX logs, allowing for quick and efficient collection of statistics on requests, responses, and the sizes of transmitted data. The program supports processing local files and remote logs via URL, with options for filtering by time parameters and outputting results in Markdown or AsciiDoc format.

## Features

- **Counting the Total Number of Requests**: Analyzes the total number of requests in the logs.
- **Identifying the Most Frequently Requested Resources**: Displays a list of URLs with the highest number of requests.
- **Identifying the Most Frequent Response Codes**: Shows the distribution of HTTP response codes (e.g., 200, 404, 500, etc.).
- **Calculating the Average Response Size**: Calculates the average size of the server's response.

## Technologies Used

- **Java**: The main programming language for the project implementation.
- **Java NIO (New I/O)**: For efficient log file reading.
- **Java Time API**: For working with dates and time.
- **Java Collections**: For gathering and processing statistics.
- **Markdown and AsciiDoc**: Formats for report output.

## Output Examples

### General Information

|        Metric        |     Value    |
|:---------------------:|-------------:|
|       File(s)        | `access.log`  |
|    Start Date        |  31.08.2023  |
|    End Date          |        -     |
|  Number of Requests  |     10,000   |
| Average Response Size|       500b   |

### Requested Resources

|     Resource     | Count |
|:----------------:|------:|
|  `/index.html`   | 5,000 |
|  `/about.html`   | 2,000 |
| `/contact.html`  | 1,000 |

### Response Codes

| Code |          Name         | Count  |
|:----:|:---------------------:|-------:|
| 200  |          OK           |  8,000 |
| 404  |       Not Found       |  1,000 |
| 500  | Internal Server Error |    500 |

## Architecture

The program is built on a pipeline principle, where:

1. User input (file names, URLs, etc.) is processed and converted into a stream of records `Stream<LogRecord>`.
2. The stream is analyzed, and a report `LogReport` is generated.
3. The report is written to a file in Markdown or AsciiDoc format.

## NGINX Log Format

The NGINX log format:
```
$remote_addr - $remote_user [$time_local] "$request" $status $body_bytes_sent "$http_referer" "$http_user_agent"
```

## Additional Features

- Log filtering by time (arguments `--from` and `--to`).
- Report format selection (argument `--format`).
