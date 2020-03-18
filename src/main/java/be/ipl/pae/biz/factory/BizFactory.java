package be.ipl.pae.biz.factory;

import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.biz.dto.PaysDto;
import be.ipl.pae.biz.dto.UserDto;

public interface BizFactory {

  UserDto getUserDto();

  MobiliteDto getMobiliteDto();

  PartenaireDto getPartenaireDto();

  PaysDto getPaysDto();

  DocumentDepartDto getDocumentDepartDto();

  DocumentRetourDto getDocumentRetourDto();

}
