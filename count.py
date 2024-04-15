import os

def count_lines_in_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        return sum(1 for line in file)

def count_java_lines(root_dir):
    total_lines = 0
    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                try:
                    line_count = count_lines_in_file(file_path)
                    print(f"{file_path}: {line_count} lines")
                    total_lines += line_count
                except Exception as e:
                    print(f"Error reading {file_path}: {e}")

    print(f"Total lines in project: {total_lines}")

if __name__ == "__main__":
    root_directory = os.path.dirname(os.path.abspath(__file__))
    count_java_lines(root_directory)