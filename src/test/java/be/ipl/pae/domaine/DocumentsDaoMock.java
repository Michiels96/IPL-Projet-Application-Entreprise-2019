package be.ipl.pae.domaine;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.dal.DocumentsDao;
import be.ipl.pae.exceptions.DalException;

public class DocumentsDaoMock implements DocumentsDao {

  @Inject
  private BizFactory bf;
  private int excInit = 0;
  private int excLast = 0;
  private int excLast2 = 0;

  @Override
  public DocumentDepartDto findDocumentsDepartByMobiliteId(MobiliteDto mob) {
    if (mob == null) {
      return null;
    }
    if (mob.getFinancement() == 42) {
      return null;
    }
    return bf.getDocumentDepartDto();
  }

  @Override
  public DocumentRetourDto findDocumentsRetourByMobiliteId(MobiliteDto mob) {
    if (mob == null) {
      return null;
    }
    if (mob.getFinancement() == 42) {
      return null;
    }
    return bf.getDocumentRetourDto();
  }

  @Override
  public void insertDocumentRetour() {
    if (excInit == 1) {
      throw new DalException();
    }
    excInit++;
  }

  @Override
  public void insertDocumentDepart() {

  }

  @Override
  public int getLastInsertIdDocumentDepart() {
    if (excLast == 1) {
      throw new DalException();
    }
    excLast++;
    return 0;
  }

  @Override
  public int getLastInsertIdDocumentRetour() {
    if (excLast2 == 1) {
      throw new DalException();
    }
    excLast2++;
    return 0;
  }

  @Override
  public DocumentDepartDto getDocumentDepartById(int idDoc) {
    if (idDoc == 42) {
      throw new DalException();
    }
    return null;
  }

  @Override
  public DocumentRetourDto getDocumentRetourById(int idDoc) {
    if (idDoc == 42) {
      throw new DalException();
    }
    return null;
  }

  @Override
  public void updateDocumentDepart(DocumentDepartDto doc) {
    if (doc.getIdDocumentsDepart() == 42) {
      throw new DalException();
    }
  }

  @Override
  public void updateDocumentRetour(DocumentRetourDto doc) {
    if (doc.getIdDocumentsRetour() == 42) {
      throw new DalException();
    }
  }

}
