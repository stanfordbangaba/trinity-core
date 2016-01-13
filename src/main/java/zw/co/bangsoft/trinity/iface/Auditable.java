package zw.co.bangsoft.trinity.iface;

import zw.co.bangsoft.trinity.model.Audit;

public interface Auditable {
  Long getId();
  Audit getAudit();
  void setAudit(Audit audit);
  String getAuditTrail();
}
