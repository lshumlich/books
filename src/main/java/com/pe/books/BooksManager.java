package com.pe.books;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.report.EntryByAccount;

public class BooksManager {
    static BooksManager instance = null;
    static {
        instance = new BooksManager();
        instance.startup();
    }

    private BooksManager() {
    }

    private void startup() {
    }

    public static BooksManager getInstance() {
        return BooksManager.instance;
    }

    public Entry addOrUpdateEntryDto(EntryDto entryDto) throws EditException {
        EntryValidator entryValidator = new EntryValidator();
        Entry entry = entryValidator.validate(entryDto);
        this.addOrUpdateEntry(entry);
        return entry;
    }

    public Account addOrUpdateAccountDto(AccountDto accountDto) throws EditException {
        AccountValidator accountValidator = new AccountValidator();
        Account account = accountValidator.validateDto(accountDto);
        this.addOrUpdateAccount(account);
        return account;
    }

    public void addOrUpdateEntry(Entry entry) throws EditException {
        Session session = HibernateUtil.getSessionFactory().openSession();
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        
        Entry entryToAddOrUpdate = null;
        EditException editException = new EditException("Edit Error with Entry.");

        if (entry.getEntryId() != null && entry.getEntryId().longValue() > 0) {
            // we have an update
            entryToAddOrUpdate = (Entry) session.get(Entry.class, entry.getEntryId());
            if (entryToAddOrUpdate == null) {
                editException.addError("", "Entry not found for update: " + entry.getEntryId());
                throw editException;
            }
            if (entryToAddOrUpdate.getDrAccount().isAssetAccount()) {
                this.updateAssetAccount(entryToAddOrUpdate.getDrAccount().getAccountId(), entryToAddOrUpdate
                        .getAmount().negate(), session);
            }
            if (entryToAddOrUpdate.getCrAccount().isAssetAccount()) {
                this.updateAssetAccount(entryToAddOrUpdate.getCrAccount().getAccountId(), entryToAddOrUpdate
                        .getAmount(), session);
            }
            entryToAddOrUpdate.setDrAccount(entry.getDrAccount());
            entryToAddOrUpdate.setDate(entry.getDate());
            entryToAddOrUpdate.setDrAccount(entry.getDrAccount());
            entryToAddOrUpdate.setDrClear(entry.getDrClear());
            entryToAddOrUpdate.setCrAccount(entry.getCrAccount());
            entryToAddOrUpdate.setCrClear(entry.getCrClear());
            entryToAddOrUpdate.setDescription(entry.getDescription());
            entryToAddOrUpdate.setAmount(entry.getAmount());
            entryToAddOrUpdate.setCheckNo(entry.getCheckNo());

        } else {
            entryToAddOrUpdate = entry;
        }

        if (entryToAddOrUpdate.getEntryId() != null && entryToAddOrUpdate.getEntryId().longValue() == 0) {
            entryToAddOrUpdate.setEntryId(null);
        }

        session.saveOrUpdate(entryToAddOrUpdate);

        if (entryToAddOrUpdate.getDrAccount().isAssetAccount()) {
            this.updateAssetAccount(entryToAddOrUpdate.getDrAccount().getAccountId(), entryToAddOrUpdate.getAmount(),
                    session);
        }
        if (entryToAddOrUpdate.getCrAccount().isAssetAccount()) {
            this.updateAssetAccount(entryToAddOrUpdate.getCrAccount().getAccountId(), entryToAddOrUpdate.getAmount()
                    .negate(), session);
        }

        transaction.commit();
    }

    private void updateAssetAccount(Long assetId, BigDecimal amount, Session session) {
        Account account = (Account) session.load(Account.class, assetId);
        account.addToBalance(amount);
        session.update(account);
    }

    public void addOrUpdateAccount(Account account) throws EditException {
    	
        Session session = HibernateUtil.getSessionFactory().openSession();
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        
        Account accountToAddOrUpdate = null;
        EditException editException = new EditException("Edit Error with Account.");

        if (account.getAccountId() != null && account.getAccountId().longValue() > 0) {
            // we have an update
            accountToAddOrUpdate = (Account) session.get(Account.class, account.getAccountId());
            if (accountToAddOrUpdate == null) {
                editException.addError("", "Account not found for update: " + account.getAccountId());
                throw editException;
            }
            accountToAddOrUpdate.setAccountId(account.getAccountId());
            accountToAddOrUpdate.setCode(account.getCode());
            accountToAddOrUpdate.setDisplayOrder(account.getDisplayOrder());
            accountToAddOrUpdate.setType(account.getType());
            accountToAddOrUpdate.setDescription(account.getDescription());
            accountToAddOrUpdate.setCurrentBalance(account.getCurrentBalance());
            accountToAddOrUpdate.setBudget(account.getBudget());
            accountToAddOrUpdate.setActive(account.getActive());
        } else {
            accountToAddOrUpdate = account;
        }

        if (accountToAddOrUpdate.getAccountId() != null && accountToAddOrUpdate.getAccountId().longValue() == 0) {
            accountToAddOrUpdate.setAccountId(null);
        }

        session.saveOrUpdate(accountToAddOrUpdate);

        // System.out.println("Account:" + account);
        // List<Account> result = session.createQuery("from Account").list();

        transaction.commit();
    }

    public Account getAccountByCode(String s) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
//        Account account = (Account) session.createQuery("from Account where code = ?").setString(0, s).uniqueResult();
        Account account = (Account) session.createQuery("from Account where code = :code").setParameter("code", s).uniqueResult();
        transaction.commit();

        return account;
    }

    public Account getAccountById(Long accountId) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();
//        Account account = (Account) session.createQuery("from Account where accountId = ?").setLong(0, accountId)
//                .uniqueResult();
        Account account = (Account) session.createQuery("from Account where accountId = :accountId")
        		.setParameter("accountId", accountId)
                .uniqueResult();
        transaction.commit();

        return account;
    }

    public Entry getEntryById(Long entryId) throws EditException {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();
        Entry entry = (Entry) session.get(Entry.class, entryId);
        if (entry != null) {
            Hibernate.initialize(entry.getDrAccount());
            Hibernate.initialize(entry.getCrAccount());
        }
        transaction.commit();

        if (entry == null) {
            throw new EditException("Entry " + entryId + " not found.");
        }
        return entry;
    }
//
//    public List<Account> getAccounts() {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        //Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();
//
//        List<Account> result = session.createQuery("from Account").list();
//        //transaction.commit();
//
//        return result;
//    }

    public List<Account> getAccounts() {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();

        List<Account> result = session.createQuery("from Account").list();
        transaction.commit();

        return result;
    }

    public List<Entry> getEntrys() {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();

        List<Entry> result = session.createQuery("from Entry").list();
        
        transaction.commit();

        return result;
    }

    public List<EntryByAccount> getEntrysToClear(Long accountId) {
        
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        transaction.begin();
//        Transaction transaction = session.getTransaction();
//        Account account = (Account) session.createQuery("from Account where code = :code").setParameter("code", s).uniqueResult();

//        List<Entry> result = session.createQuery(
//                "from Entry where (drAccountId = ? and drClear = false) or (crAccountId = ? and crClear = false)")
//                .setLong(0, accountId).setLong(1, accountId).list();
        
        List<Entry> result = session.createQuery(
                "from Entry where (drAccountId = :accountId and drClear = false) or (crAccountId = :accountId and crClear = false)")
                .setParameter("accountId", accountId).list();

        ArrayList<EntryByAccount> displayList = new ArrayList<EntryByAccount>();
        Iterator<Entry> it = result.iterator();
        Entry entry;
        while (it.hasNext()) {
            entry = it.next();
            displayList.add(new EntryByAccount(accountId, entry));
        }

        transaction.commit();

        return displayList;
    }

    public int countEntrysForAccount(Long accountId) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();

        Long count = null;

        Iterator<Object> result = session.createQuery(
                "select count(entryId) from Entry where drAccountId = ? or crAccountId = ?").setLong(0, accountId)
                .setLong(1, accountId).list().iterator();
        while (result.hasNext()) {
            count = (Long) result.next();
        }
        transaction.commit();

        return count.intValue();
    }

    public int deleteAccount(Long accountId) throws EditException {
        int entryCount = this.countEntrysForAccount(accountId);
        if (entryCount != 0) {
            throw new EditException("Can not delete, there are " + entryCount + " entrys still using that account.");
        }
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();

        int accountRows = session.createQuery("delete Account where accountId = ?").setLong(0, accountId)
                .executeUpdate();

        if (accountRows != 1) {
            throw new EditException("Expected to delete 1 row but deleted " + accountRows + " rows.");
        }
        
        transaction.commit();

        return accountRows;
    }

    public int deleteEntry(Long entryId) throws EditException {
        Entry entry = BooksManager.getInstance().getEntryById(entryId);
        if (entry == null) {
            throw new EditException("Entry not found for delete: " + entryId);
        }

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();
//        transaction.begin();
        
        if (entry.getDrAccount().isAssetAccount()) {
            this.updateAssetAccount(entry.getDrAccount().getAccountId(), entry.getAmount().negate(), session);
        }
        if (entry.getCrAccount().isAssetAccount()) {
            this.updateAssetAccount(entry.getCrAccount().getAccountId(), entry.getAmount(), session);
        }
//        Account account = (Account) session.createQuery("from Account where code = :code").setParameter("code", s).uniqueResult();

//        int entryRows = session.createQuery("delete Entry where entryId = ?").setLong(0, entryId).executeUpdate();
        int entryRows = session.createQuery("delete Entry where entryId = :id").setParameter("id", entryId).executeUpdate();

        transaction.commit();
        
        if (entryRows != 1) {
            throw new EditException("Expected to delete 1 row but deleted " + entryRows + " rows.");
        }

        return entryRows;
    }

    public void clearEntry(Long accountId, Long entryId) throws EditException {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();
//        transaction.begin();
        Clear clear = new Clear();
        clear.setAccountId(accountId);
        clear.setEntryId(entryId);
        session.save(clear);

        transaction.commit();
    }

    public int unClearEntry(Long accountId, Long entryId) throws EditException {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();
//        transaction.begin();
//      Account account = (Account) session.createQuery("from Account where code = :code").setParameter("code", s).uniqueResult();

//        int clearedRows = session.createQuery("delete Clear where accountId = ? and entryId = ?").setLong(0, accountId)
//                .setLong(1, entryId).executeUpdate();

        int clearedRows = session.createQuery("delete Clear where accountId = :accountId and entryId = :entryId").setParameter("accountId", accountId)
                .setParameter("entryId", entryId).executeUpdate();

        if (clearedRows != 1) {
            throw new EditException("Expected to delete 1 row but deleted " + clearedRows + " rows.");
        }
        transaction.commit();

        return clearedRows;
    }

    public List<Long> getClearedEntries(Long accountId) {
        ArrayList<Long> entries = new ArrayList<Long>();
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        transaction.begin();
//        Transaction transaction = session.getTransaction();
//      Account account = (Account) session.createQuery("from Account where code = :code").setParameter("code", s).uniqueResult();

//        List<Clear> result = session.createQuery("from Clear where accountId = ?").setLong(0, accountId).list();
        List<Clear> result = session.createQuery("from Clear where accountId = :accountId").setParameter("accountId", accountId).list();

        Iterator<Clear> it = result.iterator();
        Clear clear;
        while (it.hasNext()) {
            clear = it.next();
            entries.add(clear.getEntryId());
        }

        transaction.commit();
        return entries;

    }

    public int resetClearedEntries(Long accountId) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();
//        transaction.begin();
        
//        Account account = (Account) session.createQuery("from Account where code = :code").setParameter("code", s).uniqueResult();
//        int clearRows = session.createQuery("delete Clear where accountId = ?").setLong(0, accountId).executeUpdate();
        int clearRows = session.createQuery("delete Clear where accountId = :accountId").setParameter("accountId", accountId).executeUpdate();

        transaction.commit();
        return clearRows;
    }

    public void updateClearedEntries(Long accountId, List<Long> entries) throws EditException {
        EditException editException = new EditException("Entries not found to clear");
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        Transaction transaction = session.getTransaction();

        Iterator<Long> it = entries.iterator();
        while (it.hasNext()) {

            Long entryId = it.next();
            Entry entry = (Entry) session.get(Entry.class, entryId);
            if (entry.getCrAccount().getAccountId().longValue() == accountId.longValue()) {
                entry.setCrClear(true);
            } else if (entry.getDrAccount().getAccountId().longValue() == accountId.longValue()) {
                entry.setDrClear(true);
            } else {
                editException.addError("", "Account Id: " + accountId
                        + " is not the CrAccount Id or the DrAccount Id for Entry Id: " + entryId);
            }
        }

        if (editException.getErrorCount() > 0) {
            transaction.rollback();
            throw editException;
        } else {
            transaction.commit();
        }
    }
}
