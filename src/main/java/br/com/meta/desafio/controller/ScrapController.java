package br.com.meta.desafio.controller;

import br.com.meta.desafio.service.ScrapService;
import br.com.meta.desafio.viewModel.RepositoryInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrap")
public class ScrapController {
    private final ScrapService service;

    public ScrapController(ScrapService service) {
        this.service = service;
    }

    @GetMapping("/github")
    @ResponseBody
    public RepositoryInfo scrap(@RequestParam String url) throws Exception {
        return service.scrap(url);
    }
}
