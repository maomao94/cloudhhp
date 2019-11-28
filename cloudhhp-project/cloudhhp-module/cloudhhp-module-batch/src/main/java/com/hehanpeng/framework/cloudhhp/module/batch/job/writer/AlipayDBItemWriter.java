package com.hehanpeng.framework.cloudhhp.module.batch.job.writer;
import com.hehanpeng.framework.cloudhhp.module.batch.domain.dto.AlipayTranDO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: feiweiwei
 * @description:
 * @created Date: 14:28 17/11/29.
 * @modify by:
 */
@Service
public class AlipayDBItemWriter implements ItemWriter<AlipayTranDO> {
	private static final String INSERT_ALYPAY_TRAN =
			"insert into ALIPAY_TRAN_TODAY(TRAN_ID, CHANNEL, TRAN_TYPE, COUNTER_PARTY, GOODS, AMOUNT, IS_DEBIT_CREDIT, STATE) values(?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void write(List<? extends AlipayTranDO> list) throws Exception {
		for(AlipayTranDO alipayTran : list){
			jdbcTemplate.update(INSERT_ALYPAY_TRAN,
					alipayTran.getTranId(),
					alipayTran.getChannel(),
					alipayTran.getTranType(),
					alipayTran.getCounterparty(),
					alipayTran.getGoods(),
					alipayTran.getAmount(),
					alipayTran.getIsDebitCredit(),
					alipayTran.getState());
		}
	}
}
