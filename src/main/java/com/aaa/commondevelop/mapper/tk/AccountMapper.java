package com.aaa.commondevelop.mapper.tk;

import com.aaa.commondevelop.domain.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 AccountMapper.java  2025/12/6 20:48
 */
public interface AccountMapper  extends Mapper<Account> {
    // 根据账户号查询账户（悲观锁）
    @Select("SELECT * FROM account WHERE account_no = #{accountNo} FOR UPDATE")
    Account selectForUpdateAccountNo(@Param("accountNo") String accountNo);

    @Select("SELECT * FROM account WHERE balance = #{balance} FOR UPDATE")
    Account selectByBalanceForUpdate(@Param("balance") String balance);

    // 更新账户余额
    @Update("UPDATE account SET balance = #{balance}, version = version + 1 WHERE id = #{id}")
    int updateBalance(Account account);

    // 普通查询
    @Select("SELECT * FROM account WHERE account_no = #{accountNo}")
    Account selectByAccountNo(@Param("accountNo") String accountNo);


    @Select("SELECT * FROM account WHERE id BETWEEN #{start} AND #{end} FOR UPDATE")
    List<Account> selectRangeForUpdate(@Param("start") int start, @Param("end") int end);
}
