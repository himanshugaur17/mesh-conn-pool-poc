from concurrent.futures import ThreadPoolExecutor, as_completed
from collections import Counter
from urllib.error import HTTPError, URLError
from urllib.request import urlopen
import argparse
import time


def call(url, timeout):
    start = time.time()
    try:
        with urlopen(url, timeout=timeout) as response:
            response.read()
            return response.status, round(time.time() - start, 3)
    except HTTPError as error:
        error.read()
        return error.code, round(time.time() - start, 3)
    except URLError:
        return "url_error", round(time.time() - start, 3)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--url", default="http://localhost:8080/call")
    parser.add_argument("--requests", type=int, default=20)
    parser.add_argument("--concurrency", type=int, default=20)
    parser.add_argument("--timeout", type=int, default=15)
    args = parser.parse_args()

    with ThreadPoolExecutor(max_workers=args.concurrency) as pool:
        futures = [pool.submit(call, args.url, args.timeout) for _ in range(args.requests)]
        results = [future.result() for future in as_completed(futures)]

    print("status_counts:", dict(Counter(status for status, _ in results)))
    for status, elapsed in sorted(results, key=lambda item: (str(item[0]), item[1])):
        print(f"{status}\t{elapsed}s")


if __name__ == "__main__":
    main()
