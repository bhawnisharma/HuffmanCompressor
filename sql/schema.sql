CREATE DATABASE IF NOT EXISTS huffman_db;
USE huffman_db;

CREATE TABLE IF NOT EXISTS compression_records (
  id INT AUTO_INCREMENT PRIMARY KEY,
  original_text TEXT NOT NULL,
  encoded_bits LONGTEXT NOT NULL,
  original_bits INT,
  compressed_bits INT,
  saved_percent DECIMAL(5,2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS code_table (
  id INT AUTO_INCREMENT PRIMARY KEY,
  record_id INT,
  symbol VARCHAR(10),
  frequency INT,
  huffman_code VARCHAR(255),
  FOREIGN KEY (record_id) REFERENCES compression_records(id) ON DELETE CASCADE
);