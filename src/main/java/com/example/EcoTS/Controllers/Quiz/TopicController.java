package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.Models.Topic;
import com.example.EcoTS.Services.Quiz.TopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Quiz")
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @GetMapping
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        Topic topic = topicService.getTopicById(id);
        if (topic != null) {
            return ResponseEntity.ok(topic);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        return topicService.createTopic(topic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic topicDetails) {
        Topic topic = topicService.getTopicById(id);
        if (topic != null) {
            topic.setName(topicDetails.getName());
            topic.setImage(topicDetails.getImage());
            topic.setProgress(topicDetails.getProgress());
            return ResponseEntity.ok(topicService.updateTopic(topic));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
