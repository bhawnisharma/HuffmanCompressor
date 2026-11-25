import dao.CompressionDAO;
import model.CodeEntry;
import model.CompressionRecord;
import java.util.ArrayList;
import java.util.List;

public class DAOTest {
    public static void main(String[] args) {
        CompressionDAO dao = new CompressionDAO();

        CompressionRecord r = new CompressionRecord();
        r.setOriginalText("ABRACADABRA");
        r.setEncodedBits("01001010110011101001010");
        r.setOriginalBits(88);
        r.setCompressedBits(23);
        r.setSavedPercent(73.86);

        List<CodeEntry> list = new ArrayList<>();
        list.add(new CodeEntry("A", 5, "0"));
        list.add(new CodeEntry("B", 2, "100"));
        list.add(new CodeEntry("R", 2, "101"));
        list.add(new CodeEntry("C", 1, "110"));
        list.add(new CodeEntry("D", 1, "111"));
        r.setCodeEntries(list);

        int id = dao.insertRecord(r);
        System.out.println("Inserted id: " + id);
        System.out.println("All records: " + dao.getAllRecords().size());
        System.out.println("Entries: " + dao.getRecordById(id).getCodeEntries().size());
        System.out.println("Deleted: " + dao.deleteRecord(id));
    }
}