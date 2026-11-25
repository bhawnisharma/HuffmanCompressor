package dao;

import model.CodeEntry;
import model.CompressionRecord;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompressionDAO {

    public int insertRecord(CompressionRecord r) {
        String sql1 = "INSERT INTO compression_records "
                + "(original_text, encoded_bits, original_bits, compressed_bits, saved_percent) "
                + "VALUES (?,?,?,?,?)";
        String sql2 = "INSERT INTO code_table (record_id, symbol, frequency, huffman_code) "
                + "VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, r.getOriginalText());
                ps.setString(2, r.getEncodedBits());
                ps.setInt(3, r.getOriginalBits());
                ps.setInt(4, r.getCompressedBits());
                ps.setDouble(5, r.getSavedPercent());
                ps.executeUpdate();

                int id;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getInt(1);
                }
                if (r.getCodeEntries() != null) {
                    try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
                        for (CodeEntry e : r.getCodeEntries()) {
                            ps2.setInt(1, id);
                            ps2.setString(2, e.getSymbol());
                            ps2.setInt(3, e.getFrequency());
                            ps2.setString(4, e.getHuffmanCode());
                            ps2.addBatch();
                        }
                        ps2.executeBatch();
                    }
                }
                con.commit();
                return id;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<CompressionRecord> getAllRecords() {
        List<CompressionRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM compression_records ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRecord(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public CompressionRecord getRecordById(int id) {
        String sql1 = "SELECT * FROM compression_records WHERE id = ?";
        String sql2 = "SELECT symbol, frequency, huffman_code FROM code_table WHERE record_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                CompressionRecord r = mapRecord(rs);
                List<CodeEntry> entries = new ArrayList<>();
                try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
                    ps2.setInt(1, id);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        while (rs2.next()) {
                            entries.add(new CodeEntry(rs2.getString("symbol"),
                                    rs2.getInt("frequency"), rs2.getString("huffman_code")));
                        }
                    }
                }
                r.setCodeEntries(entries);
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM compression_records WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private CompressionRecord mapRecord(ResultSet rs) throws SQLException {
        CompressionRecord r = new CompressionRecord();
        r.setId(rs.getInt("id"));
        r.setOriginalText(rs.getString("original_text"));
        r.setEncodedBits(rs.getString("encoded_bits"));
        r.setOriginalBits(rs.getInt("original_bits"));
        r.setCompressedBits(rs.getInt("compressed_bits"));
        r.setSavedPercent(rs.getDouble("saved_percent"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}