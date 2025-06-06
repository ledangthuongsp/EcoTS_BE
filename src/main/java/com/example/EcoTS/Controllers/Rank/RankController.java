package com.example.EcoTS.Controllers.Rank;

import com.example.EcoTS.Models.Rank;
import com.example.EcoTS.Models.UserRank;
import com.example.EcoTS.Services.Rank.RankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rank")
@Tag(name = "User Ranking")
@CrossOrigin
public class RankController {

    @Autowired
    private RankService rankService;
    @GetMapping
    public List<Rank> getAllRanks() {
        return rankService.getAllRanks();
    }
    // Lấy thông tin rank của người dùng
    @GetMapping("/user/{userId}")
    public UserRank getUserRank(@PathVariable Long userId) {
        return rankService.getUserRank(userId);
    }

    // Thêm rank mới
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRank(@RequestParam double rankPoint, @RequestParam String rankName) {
        rankService.addRank(rankPoint, rankName);
    }

    // Sửa rank hiện tại
    @PutMapping("/update/{rankId}")
    public void updateRank(
            @PathVariable Long rankId,
            @RequestParam double rankPoint,
            @RequestParam String rankName) {
        Rank updatedRank = new Rank();
        updatedRank.setRankPoint(rankPoint);
        updatedRank.setRankName(rankName);
        rankService.updateRank(rankId, updatedRank);
    }

    // Xóa rank
    @DeleteMapping("/delete/{rankId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRank(@PathVariable Long rankId) {
        rankService.deleteRank(rankId);
    }
}
