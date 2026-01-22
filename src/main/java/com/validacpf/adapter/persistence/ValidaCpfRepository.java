package com.validacpf.adapter.persistence;

import com.validacpf.adapter.persistence.entity.ValidaCpfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidaCpfRepository extends JpaRepository<ValidaCpfEntity, Long> {
	
	Optional<ValidaCpfEntity> findByCpf(String cpf);
}
